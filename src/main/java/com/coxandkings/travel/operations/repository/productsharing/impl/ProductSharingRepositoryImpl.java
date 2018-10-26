package com.coxandkings.travel.operations.repository.productsharing.impl;

import com.coxandkings.travel.operations.model.productsharing.ProductSharing;
import com.coxandkings.travel.operations.repository.productsharing.ProductSharingRepository;
import com.coxandkings.travel.operations.resource.productsharing.ProductSharingStatus;
import org.apache.logging.log4j.Logger;import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductSharingRepositoryImpl extends SimpleJpaRepository<ProductSharing, String> implements ProductSharingRepository {

    private static final Logger logger = LogManager.getLogger(ProductSharingRepositoryImpl.class);

    private EntityManager entityManager;

    public ProductSharingRepositoryImpl(@Qualifier("opsEntityManager") EntityManager em) {
        super(ProductSharing.class, em);
        entityManager = em;
    }


    @Transactional
    @Override
    public ProductSharing saveOrUpdateProductSharing(ProductSharing productSharing) {
        try {
            productSharing = this.saveAndFlush(productSharing);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(" public ProductSharing saveOrUpdateProductSharing(ProductSharing productSharing) , exception raised : " + e);
        }
        return productSharing;
    }

    @Override
    public List<ProductSharing> findByStatus(String status) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        criteriaBuilder.createQuery(ProductSharing.class);
        CriteriaQuery<ProductSharing> productSharingQuery = null;
        Root<ProductSharing> root = null;
        TypedQuery<ProductSharing> query = null;
        try {
            if (entityManager != null) {
                criteriaBuilder = this.entityManager.getCriteriaBuilder();
                if (criteriaBuilder != null) {
                    productSharingQuery = criteriaBuilder.createQuery(ProductSharing.class);
                    root = productSharingQuery.from(ProductSharing.class);
                    Predicate bookRefNoP = criteriaBuilder.equal(root.get("status"), ProductSharingStatus.valueOf(status));
                    productSharingQuery.select(root).where(bookRefNoP);
                }
            }
            query = this.entityManager.createQuery(productSharingQuery);
            if (query != null) {
                if (query.getResultList() != null) {
                    return query.getResultList();
                } else {
                    return new ArrayList<ProductSharing>();
                }
            } else {
                return new ArrayList<ProductSharing>();
            }
        } catch (NonUniqueResultException e) {
            e.printStackTrace();
            logger.info("  public List<ProductSharing> findByStatus(String status) , exception raised : " + e);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(" public List<ProductSharing> findByStatus(String status) , exception raised : " + e);
            return new ArrayList<ProductSharing>();
        }


    }

    @Override
    public List<ProductSharing> findByStatus(String status, String fromSerialNumber) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        criteriaBuilder.createQuery(ProductSharing.class);
        CriteriaQuery<ProductSharing> productSharingQuery = null;
        Root<ProductSharing> root = null;
        TypedQuery<ProductSharing> query = null;
        try {
            if (entityManager != null) {
                criteriaBuilder = this.entityManager.getCriteriaBuilder();
                if (criteriaBuilder != null) {
                    productSharingQuery = criteriaBuilder.createQuery(ProductSharing.class);
                    root = productSharingQuery.from(ProductSharing.class);
                    Predicate bookRefNoP = criteriaBuilder.equal(root.get("status"), ProductSharingStatus.valueOf(status));
                    Predicate fromSerialNumberP = criteriaBuilder.equal(root.get("fromSerialNumber"), fromSerialNumber);
                    productSharingQuery.select(root).where(criteriaBuilder.and(bookRefNoP, fromSerialNumberP));
                }
            }
            query = this.entityManager.createQuery(productSharingQuery);
            if (query != null) {
                if (query.getResultList() != null) {
                    return query.getResultList();
                } else {
                    return new ArrayList<ProductSharing>();
                }
            } else {
                return new ArrayList<ProductSharing>();
            }
        } catch (NonUniqueResultException e) {
            e.printStackTrace();
            logger.info(" public List<ProductSharing> findByStatus(String status, String fromSerialNumber) , exception raised : " + e);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(" public List<ProductSharing> findByStatus(String status, String fromSerialNumber) , exception raised : " + e);
            return new ArrayList<ProductSharing>();
        }


    }

    @Override
    public List<ProductSharing> findByHash(String hash) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        criteriaBuilder.createQuery(ProductSharing.class);
        CriteriaQuery<ProductSharing> productSharingQuery = null;
        Root<ProductSharing> root = null;
        TypedQuery<ProductSharing> query = null;
        try {
            if (entityManager != null) {
                criteriaBuilder = this.entityManager.getCriteriaBuilder();
                if (criteriaBuilder != null) {
                    productSharingQuery = criteriaBuilder.createQuery(ProductSharing.class);
                    root = productSharingQuery.from(ProductSharing.class);
                    Predicate bookRefNoP = criteriaBuilder.equal(root.get("hash"), hash);
                    productSharingQuery.select(root).where(bookRefNoP);
                }
            }
            query = this.entityManager.createQuery(productSharingQuery);
            if (query != null) {
                if (query.getResultList() != null) {
                    return query.getResultList();
                } else {
                    return new ArrayList<ProductSharing>();
                }
            } else {
                return new ArrayList<ProductSharing>();
            }
        } catch (NonUniqueResultException e) {
            e.printStackTrace();
            logger.info("  public List<ProductSharing> findByHash(String hash)  , exception raised : " + e);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("  public List<ProductSharing> findByHash(String hash)  , exception raised : " + e);
            return new ArrayList<ProductSharing>();
        }


    }


    @Override
    public ProductSharing findByBookRefAndOrderNo(String bookRefNo, String orderNo) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        criteriaBuilder.createQuery(ProductSharing.class);
        CriteriaQuery<ProductSharing> productSharingQuery = null;
        Root<ProductSharing> root = null;
        TypedQuery<ProductSharing> query = null;
        try {
            if (entityManager != null) {
                criteriaBuilder = this.entityManager.getCriteriaBuilder();
                if (criteriaBuilder != null) {
                    productSharingQuery = criteriaBuilder.createQuery(ProductSharing.class);
                    root = productSharingQuery.from(ProductSharing.class);
                    Predicate bookRefNoP = criteriaBuilder.equal(root.get("fromBookRefNo"), bookRefNo);
                    Predicate orderNoP = criteriaBuilder.equal(root.get("fromOrderID"), orderNo);
                    productSharingQuery.select(root).where(criteriaBuilder.and(bookRefNoP, orderNoP));
                }
            }
            query = this.entityManager.createQuery(productSharingQuery);
            if (query != null) {
                if (query.getSingleResult() != null) {
                    return query.getSingleResult();
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } catch (NonUniqueResultException e) {
            e.printStackTrace();
            logger.info("   public ProductSharing findByBookRefAndOrderNo(String bookRefNo, String orderNo)  , exception raised : " + e);
            return query.getResultList().get(0);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("   public ProductSharing findByBookRefAndOrderNo(String bookRefNo, String orderNo)  , exception raised : " + e);
            return null;
        }
    }


    @Override
    public ProductSharing findByBookRefAndOrderNoAndSerialNo(String bookRefNo, String orderID, String serialNumber, String fromPassengerId, String toSerialNumber) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        criteriaBuilder.createQuery(ProductSharing.class);
        CriteriaQuery<ProductSharing> productSharingQuery = null;
        Root<ProductSharing> root = null;
        TypedQuery<ProductSharing> query = null;
        try {
            if (entityManager != null) {
                criteriaBuilder = this.entityManager.getCriteriaBuilder();
                if (criteriaBuilder != null) {
                    productSharingQuery = criteriaBuilder.createQuery(ProductSharing.class);
                    root = productSharingQuery.from(ProductSharing.class);
                    Predicate bookRefNoP = criteriaBuilder.equal(root.get("fromBookRefNo"), bookRefNo);
                    Predicate orderIDP = criteriaBuilder.equal(root.get("fromOrderID"), orderID);
                    Predicate serialNumberP = criteriaBuilder.equal(root.get("fromSerialNumber"), serialNumber);
                    Predicate toSerialNumberP = criteriaBuilder.equal(root.get("passengerId"), toSerialNumber);
                    Predicate fromPassengerIdP = criteriaBuilder.equal(root.get("fromPassengerId"), fromPassengerId);
                    productSharingQuery.select(root).where(criteriaBuilder.and(bookRefNoP, orderIDP, serialNumberP, toSerialNumberP, fromPassengerIdP));
                }
            }
            query = this.entityManager.createQuery(productSharingQuery);
            if (query != null) {
                if (query.getSingleResult() != null) {
                    return query.getSingleResult();
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } catch (NonUniqueResultException e) {
            logger.info("  public ProductSharing findByBookRefAndOrderNoAndSerialNo(String bookRefNo, String orderID, String serialNumber, String fromPassengerId, String toSerialNumber)  , exception raised : " + e);
            e.printStackTrace();
            return query.getResultList().get(0);
        } catch (Exception e) {
            logger.info("  public ProductSharing findByBookRefAndOrderNoAndSerialNo(String bookRefNo, String orderID, String serialNumber, String fromPassengerId, String toSerialNumber)  , exception raised : " + e);
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public ProductSharing findProductSharingByHash(String hash) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        criteriaBuilder.createQuery(ProductSharing.class);
        CriteriaQuery<ProductSharing> productSharingQuery = null;
        Root<ProductSharing> root = null;
        TypedQuery<ProductSharing> query = null;
        try {
            if (entityManager != null) {
                criteriaBuilder = this.entityManager.getCriteriaBuilder();
                if (criteriaBuilder != null) {
                    productSharingQuery = criteriaBuilder.createQuery(ProductSharing.class);
                    root = productSharingQuery.from(ProductSharing.class);
                    Predicate bookRefNoP = criteriaBuilder.equal(root.get("hash"), hash);
                    productSharingQuery.select(root).where(bookRefNoP);
                }
            }
            query = this.entityManager.createQuery(productSharingQuery);
            if (query != null) {
                if (query.getSingleResult() != null) {
                    return query.getSingleResult();
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } catch (NonUniqueResultException e) {
            logger.info("  public ProductSharing findProductSharingByHash(String hash)  , exception raised : " + e);
            e.printStackTrace();
            return query.getResultList().get(0);
        } catch (Exception e) {
            logger.info("  public ProductSharing findProductSharingByHash(String hash) , exception raised : " + e);
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ProductSharing findProductSharingBySecondRef(String secondRef, boolean isConverted) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        criteriaBuilder.createQuery(ProductSharing.class);
        CriteriaQuery<ProductSharing> productSharingQuery = null;
        Root<ProductSharing> root = null;
        TypedQuery<ProductSharing> query = null;
        try {
            if (entityManager != null) {
                criteriaBuilder = this.entityManager.getCriteriaBuilder();
                if (criteriaBuilder != null) {
                    productSharingQuery = criteriaBuilder.createQuery(ProductSharing.class);
                    root = productSharingQuery.from(ProductSharing.class);
                    Predicate bookRefNoP = criteriaBuilder.equal(root.get("secondRef"), secondRef);
                    Predicate isConvertedP = criteriaBuilder.equal(root.get("isConverted"), isConverted);
                    productSharingQuery.select(root).where(criteriaBuilder.and(bookRefNoP, isConvertedP));
                }
            }
            query = this.entityManager.createQuery(productSharingQuery);
            if (query != null) {
                if (query.getSingleResult() != null) {
                    return query.getSingleResult();
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } catch (NonUniqueResultException e) {
            e.printStackTrace();
            logger.info("   public ProductSharing findProductSharingBySecondRef(String secondRef, boolean isConverted) , exception raised : " + e);
            return query.getResultList().get(0);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("   public ProductSharing findProductSharingBySecondRef(String secondRef, boolean isConverted) , exception raised : " + e);
            return null;
        }
    }
}
