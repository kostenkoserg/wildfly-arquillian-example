package com.fasto.datamanager;

import com.fasto.datamanager.model.SystemPropertyEntity;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Remote(ConfigurationService.class)
@Stateless(name = ConfigurationService.CONFIGURATION_SERVICE_NAME)
public class ConfigurationServiceImpl implements ConfigurationService {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)

    public String getProperty(String name) {
        Optional<SystemPropertyEntity> propertyEntity = Optional.ofNullable(entityManager.find(SystemPropertyEntity.class, name));
        return propertyEntity.isPresent() ? propertyEntity.get().getValue() : null;
    }
}
