/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.mascotas.test.persistence;

import co.edu.uniandes.csw.mascotas.entities.PublicidadEntity;
import co.edu.uniandes.csw.mascotas.entities.MultimediaEntity;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;
import co.edu.uniandes.csw.mascotas.persistence.PublicidadPersistence;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Before;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

/**
 *
 * @author German Rozo
 */
@RunWith(Arquillian.class)
public class PublicidadPersistenceTest {

    @Inject
    PublicidadPersistence pp;

    @Inject
    UserTransaction utx;
    
    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClass(PublicidadEntity.class)
                .addClass(PublicidadPersistence.class)
                .addAsManifestResource("META-INF/persistence.xml", "persistence.xml")
                .addAsManifestResource("META-INF/beans.xml", "beans.xml");
    }

    @PersistenceContext
    protected EntityManager em;

    private List<PublicidadEntity> data = new ArrayList<>();
    
    
    @Before
    public void configTest() {
        try {
            utx.begin();
            em.joinTransaction();
            clearData();
            utx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                utx.rollback();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }
    
    private void clearData() {
        em.createQuery("delete from PublicidadEntity").executeUpdate();
    }
    
    @Test
    public void findAllTest() {
        
        PodamFactory factory = new PodamFactoryImpl();

        ArrayList< PublicidadEntity> resultados = new ArrayList();

        int j = (int) (Math.random() * ((100 - 1) + 1)) + 1;
        
        for (int i = 0; i < j; i++) {
            PublicidadEntity publicidad = factory.manufacturePojo(PublicidadEntity.class);
            PublicidadEntity resultado = pp.create(publicidad);
            Assert.assertNotNull(resultado);
            resultados.add(resultado);
        }

        List<PublicidadEntity> r = pp.findAll();
        Assert.assertEquals(j, r.size());
        Iterator iter = resultados.iterator();

        while (iter.hasNext()) {
            PublicidadEntity next = (PublicidadEntity) iter.next();
            Assert.assertTrue(r.contains(next));
        }
    }
    
    @Test
    public void createTest() 
    {
        PodamFactory factory = new PodamFactoryImpl();
        PublicidadEntity publicidad = factory.manufacturePojo(PublicidadEntity.class);
        PublicidadEntity resultado = pp.create(publicidad);
        ArrayList<MultimediaEntity> al= new ArrayList<>();
        al.add(new MultimediaEntity());
        Assert.assertEquals(0, publicidad.getMultimedia().size()
                );
        publicidad.setMultimedia(al);
        Assert.assertNotNull(resultado);

        PublicidadEntity entity = em.find(PublicidadEntity.class, resultado.getId());
        Assert.assertEquals(publicidad.getMensaje(),
                entity.getMensaje());
        
               
        Assert.assertEquals(1, publicidad.getMultimedia().size());

    }

    @Test
    public void findTest() {
        PodamFactory factory = new PodamFactoryImpl();

        PublicidadEntity publicidad = factory.manufacturePojo(PublicidadEntity.class);
        PublicidadEntity resultado = pp.create(publicidad);
        Assert.assertNotNull(resultado);
        PublicidadEntity r = pp.find(resultado.getId());
        Assert.assertNotNull(r);
    }

    

    @Test
    public void updateTest() {

        PodamFactory factory = new PodamFactoryImpl();
        PublicidadEntity o = factory.manufacturePojo(PublicidadEntity.class);
        PublicidadEntity m = factory.manufacturePojo(PublicidadEntity.class);

        o = pp.create(o);

        o.setCosto(m.getCosto());
        o.setDiasPorSemana(m.getDiasPorSemana());
        o.setFecchaFin(m.getFecchaFin());
        o.setFechaInicio(m.getFechaInicio());
        o.setMensaje(m.getMensaje());

        pp.update(o);

        PublicidadEntity a = pp.find(o.getId());

        Assert.assertEquals(o.getId(), a.getId());
        Assert.assertEquals(o.getCosto(), a.getCosto());
        Assert.assertEquals(o.getDiasPorSemana(), a.getDiasPorSemana());
        Assert.assertEquals(o.getMensaje(), a.getMensaje());
        Assert.assertEquals(o.getFechaInicio(), a.getFechaInicio());
        Assert.assertEquals(o.getFecchaFin(), a.getFecchaFin());

    }

    @Test
    public void deleteTest() {
        PodamFactory factory = new PodamFactoryImpl();
        PublicidadEntity p = factory.manufacturePojo(PublicidadEntity.class);

        p = pp.create(p);
        pp.delete(p.getId());

        Assert.assertNull(pp.find(p.getId()));
    }
}
