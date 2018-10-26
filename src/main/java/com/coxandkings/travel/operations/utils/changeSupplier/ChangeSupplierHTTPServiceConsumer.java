package com.coxandkings.travel.operations.utils.changeSupplier;

import com.coxandkings.travel.operations.utils.xml.XMLTransformer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.MediaType;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

public class ChangeSupplierHTTPServiceConsumer {

    private static final Logger logger = LogManager.getLogger(ChangeSupplierHTTPServiceConsumer.class);
    public static final long DEFAULT_SERVICE_TIMEOUT_MILLIS = 30000;
    // The below constants are already defined in javax.ws.rs.HttpMethod and org.springframework.http.HttpMethod classes. However,
    // using those constants would add dependency on those packages. Therefore, redefining the constants for HTTP methods again.
    // It is preferable to use whatever constants are available from javax.ws.rs classes as it is Java EE.
    public static final String HTTP_METHOD_DELETE = "DELETE";
    public static final String HTTP_METHOD_GET = "GET";
    public static final String HTTP_METHOD_HEAD = "HEAD";
    public static final String HTTP_METHOD_OPTIONS = "OPTIONS";
    public static final String HTTP_METHOD_POST = "POST";
    public static final String HTTP_METHOD_PUT = "PUT";

    private static final String HTTP_ENCODING_DEFLATE = "deflate";
    private static final String HTTP_ENCODING_GZIP = "gzip";
    private static final String HTTP_ENCODING_XGZIP = "x-gzip";
    private static final String HTTP_HEADER_ACCEPT_ENCODING = "Accept-Encoding";
    private static final String HTTP_HEADER_ACCEPT_ENCODING_VALUES = HTTP_ENCODING_DEFLATE.concat(", ").concat(HTTP_ENCODING_GZIP).concat(", ").concat(HTTP_ENCODING_XGZIP);
    private static final Pattern PATTERN_CONTENT_TYPE_CHARSET = Pattern.compile("(charset[ ]*=[ ]*[^; ]+)");

    public static Element consumeXMLService(String tgtSysId, URL tgtSysURL, Map<String, String> httpHdrs, Element reqElem) throws Exception {
        return consumeXMLService(tgtSysId, tgtSysURL, null, httpHdrs, HTTP_METHOD_POST, DEFAULT_SERVICE_TIMEOUT_MILLIS, reqElem);
    }

    public static Element consumeXMLService(String tgtSysId, URL tgtSysURL,  Element reqElem) throws Exception {
        Map<String, String> httpHdrs = new HashMap<>();
        httpHdrs.put("content-type", MediaType.APPLICATION_XML_VALUE);
        return consumeXMLService(tgtSysId, tgtSysURL, null, httpHdrs, HTTP_METHOD_POST, DEFAULT_SERVICE_TIMEOUT_MILLIS, reqElem);
    }

    public static Element consumeXMLService(String tgtSysId, URL tgtSysURL, Proxy httpProxy, Map<String, String> httpHdrs, String httpMethod, long svcTimeout, Element reqElem) throws Exception {
        String resStr = consumeServiceV3(tgtSysId, tgtSysURL, httpProxy, httpHdrs, httpMethod, svcTimeout, XMLTransformer.toString(reqElem)).getPayloadString();
        if (resStr != null) {
            try {
                //return XMLTransformer.getNewDocumentBuilder().parse(new ByteArrayInputStream(resStr.getBytes(Charset.forName("UTF-8")))).getDocumentElement();
                return XMLTransformer.getNewDocumentBuilder().parse(new InputSource(new StringReader(resStr))).getDocumentElement();
            }
            catch (Exception x) {
                logger.warn(String.format("%s_ERR Error parsing XML response from service <%s>: %s", tgtSysId, tgtSysURL, resStr), x);
            }
        }

        return null;
    }

    private static HttpTemplate consumeServiceV3(String tgtSysId, URL tgtSysURL, Proxy httpProxy, Map<String, String> httpHdrs, String httpMethod, long serviceTimeout, String payload) {
        HttpURLConnection svcConn = null;
        HttpTemplate httpTemplate = new HttpTemplate();

        try {
            svcConn = (HttpURLConnection) ((httpProxy!=null)? tgtSysURL.openConnection(httpProxy) : tgtSysURL.openConnection());
            svcConn.setRequestMethod(httpMethod);
            svcConn.setReadTimeout((int) serviceTimeout);

            svcConn.setRequestProperty(HTTP_HEADER_ACCEPT_ENCODING ,HTTP_HEADER_ACCEPT_ENCODING_VALUES);

            if(httpHdrs!=null)
            {
                Set<Map.Entry<String,String>> httpHeaders = httpHdrs.entrySet();
                if(httpHeaders!=null && httpHeaders.size()>0)
                {
                    Iterator<Map.Entry<String,String>> httpHeadersIter = httpHeaders.iterator();
                    while(httpHeadersIter.hasNext()){
                        Map.Entry<String,String> entry = httpHeadersIter.next();
                        svcConn.setRequestProperty(entry.getKey(),entry.getValue());
                    }
                }
            }

            if (logger.isInfoEnabled()) {
                logger.info(String.format("%s_RQ = %s", tgtSysId, payload));
            }

            if(HTTP_METHOD_POST.equalsIgnoreCase(httpMethod) || HTTP_METHOD_PUT.equalsIgnoreCase(httpMethod)) {

                svcConn.setDoOutput(true);
                OutputStream outputStream = svcConn.getOutputStream();
                outputStream.write(payload.getBytes());
                outputStream.flush();
                outputStream.close();
            }

            int resCode = svcConn.getResponseCode();
            httpTemplate.setStatusCode(resCode);
            httpTemplate.setHtttpHeaders(svcConn.getHeaderFields());
            logger.debug(String.format("Receiving response from %s with HTTP response status: %s", tgtSysId, resCode));

            if (resCode == HttpURLConnection.HTTP_OK || (HttpURLConnection.HTTP_ACCEPTED == resCode && "COMMCACHE".equals(tgtSysId) || (HttpURLConnection.HTTP_CREATED == resCode && "OPSTODO".equals(tgtSysId)))) {
                String resStr = readInputStreamAsString(svcConn.getInputStream(), svcConn.getContentType(), svcConn.getContentEncoding());
                if (logger.isInfoEnabled()) {
                    logger.info(String.format("%s_RS = %s", tgtSysId, resStr));
                }
                httpTemplate.setPayload(resStr);
            }
            else if (resCode >=400) {
                InputStream errStream = svcConn.getErrorStream();
                String errStr =  new BufferedReader(new InputStreamReader(errStream)).lines().collect(Collectors.joining("\n"));
                httpTemplate.setError(errStr);
            }

        } catch (IOException e) {
            logger.warn(String.format("%s_ERR Service <%s> Consume Error", tgtSysId, tgtSysURL), e);
        }
        finally {
            if (svcConn != null) {
                svcConn.disconnect();
            }
        }

        return httpTemplate;
    }

    private static String readInputStreamAsString(InputStream inStream, String contentType, String contentEncoding) throws IOException {
        ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();

        String[] contentEncodings = (contentEncoding != null) ? contentEncoding.split(", ") : new String[0];
        try (InputStream readStream = getInputStreamForContentEncodings(inStream, Arrays.asList(contentEncodings))) {
            int readBytesLen = 0;
            byte[] readBytesBuffer = new byte[2048];
            while ((readBytesLen = readStream.read(readBytesBuffer)) > 0) {
                byteOutStream.write(readBytesBuffer, 0, readBytesLen);
            }
        }

        Charset strCharset = Charset.defaultCharset();
        if (contentType != null) {
            // Content-Type string converted to lower case because RFC7231 (sections 3.1.1.1 & 3.1.1.2)
            // specifies parameter name and value tokens as case-insensitive
            Matcher matcher = PATTERN_CONTENT_TYPE_CHARSET.matcher(contentType.toLowerCase());
            if (matcher.find()) {
                // The replaceAll method removes spaces before and after '=' character
                String charsetStr = matcher.group().replaceAll("[ ]*", "").substring("charset=".length());
                try {
                    strCharset = Charset.forName(charsetStr);
                }
                catch (Exception x) {
                    logger.warn(String.format("Character set %s not found", charsetStr), x);
                }
            }
        }

        byte[] inStreamBytes = byteOutStream.toByteArray();
        return (new String(inStreamBytes, strCharset)).replaceAll("\n", "");
    }

    private static InputStream getInputStreamForContentEncodings(InputStream inStream, List<String> contentEncodings) throws IOException {
        if (contentEncodings == null || contentEncodings.size() == 0) {
            return inStream;
        }

        String contentEncoding = contentEncodings.remove(contentEncodings.size() - 1);
        if (HTTP_ENCODING_DEFLATE.equals(contentEncoding)) {
            return getInputStreamForContentEncodings(new InflaterInputStream(inStream), contentEncodings);
        }
        if (HTTP_ENCODING_GZIP.equals(contentEncoding) || HTTP_ENCODING_XGZIP.equals(contentEncoding)) {
            return getInputStreamForContentEncodings(new GZIPInputStream(inStream), contentEncodings);
        }

        return inStream;
    }

}
