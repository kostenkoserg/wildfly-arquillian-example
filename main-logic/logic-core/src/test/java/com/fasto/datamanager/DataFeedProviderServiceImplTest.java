package com.fasto.datamanager;

import com.fasto.datamanager.dto.DataFeedProviderDto;
import com.fasto.datamanager.dto.wrappers.ResponseWrapper;
import com.fasto.datamanager.model.DataFeedProviderEntity;
import com.fasto.datamanager.model.DataFeedSchedulesEntity;
import com.fasto.datamanager.paging.QueryRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.Persistence;
import java.util.List;

/**
 * @author kostenko
 */
public class DataFeedProviderServiceImplTest {

    DataFeedProviderServiceImpl impl = new DataFeedProviderServiceImpl();

    @Before
    public void init() {
        impl.entityManager = Persistence.createEntityManagerFactory("fastoDSTest").createEntityManager();
    }

    @Test
    public void getDataFeedProvidersTest() {

        DataFeedSchedulesEntity  schedulesEntity = new DataFeedSchedulesEntity();
        schedulesEntity.setYear("2018");
        
        DataFeedProviderEntity providerEntity = new DataFeedProviderEntity();
        providerEntity.setName("name");

        impl.entityManager.getTransaction().begin();
        impl.entityManager.persist(schedulesEntity);
        providerEntity.setDataFeedSchedules(schedulesEntity);
        impl.entityManager.persist(providerEntity);
        impl.entityManager.getTransaction().commit();

        ResponseWrapper<DataFeedProviderDto> responseWrapper = impl.getDataFeedProviders(new QueryRequest());
        List<DataFeedProviderDto> feedProviderDtos = responseWrapper.getData();

        Assert.assertEquals(1, feedProviderDtos.size());
        Assert.assertEquals("name", feedProviderDtos.get(0).getName());
        Assert.assertEquals("2018", feedProviderDtos.get(0).getDataFeedSchedulesDto().getYear());
    }

}
