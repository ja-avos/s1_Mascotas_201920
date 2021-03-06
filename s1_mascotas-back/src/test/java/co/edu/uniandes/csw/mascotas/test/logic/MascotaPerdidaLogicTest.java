/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.mascotas.test.logic;

import co.edu.uniandes.csw.mascotas.entities.MascotaPerdidaEntity;
import co.edu.uniandes.csw.mascotas.persistence.MascotaPerdidaPersistence;
import co.edu.uniandes.csw.mascotas.ejb.MascotaPerdidaLogic;
import co.edu.uniandes.csw.mascotas.entities.MultimediaEntity;
import co.edu.uniandes.csw.mascotas.entities.RecompensaEntity;
import co.edu.uniandes.csw.mascotas.entities.UsuarioEntity;
import co.edu.uniandes.csw.mascotas.exceptions.BusinessLogicException;
import co.edu.uniandes.csw.mascotas.podam.TipoEspecies;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import junit.framework.Assert;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

/**
 *
 * @author Lily
 */
@RunWith(Arquillian.class)
public class MascotaPerdidaLogicTest {
    
    private PodamFactory fac = new PodamFactoryImpl();
    
    @PersistenceContext(unitName = "mascotasPU")
    protected EntityManager em;
    
    @Inject
    private MascotaPerdidaLogic mascotaLogic;
    
    @Deployment
    public static JavaArchive createDeployment()
    {
        return ShrinkWrap.create(JavaArchive.class)
                .addPackage(MascotaPerdidaEntity.class.getPackage())
                .addPackage(MascotaPerdidaLogic.class.getPackage())
                .addPackage(MascotaPerdidaPersistence.class.getPackage())
                .addAsManifestResource("META-INF/persistence.xml", "persistence.xml")
                .addAsManifestResource("META-INF/beans.xml", "beans.xml");
    }
    @Inject
    private UserTransaction utx;
    private List<MascotaPerdidaEntity> data = new ArrayList<>();
     /**
     * Configuración inicial de la prueba.
     */
    @Before
    public void configTest() {
        try {
            utx.begin();
            clearData();
            insertData();
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
    /**
     * Limpia las tablas que están implicadas en la prueba.
     */
    private void clearData() {
        em.createQuery("delete from MascotaPerdidaEntity").executeUpdate();
        em.createQuery("delete from UsuarioEntity").executeUpdate();
        em.createQuery("delete from MultimediaEntity").executeUpdate();
        em.createQuery("delete from RecompensaEntity").executeUpdate();
    }
    /**
     * Inserta los datos iniciales para el correcto funcionamiento de las
     * pruebas.
     */
    private void insertData() {
        for (int i = 0; i < 3; i++) {
            MascotaPerdidaEntity entity = fac.manufacturePojo(MascotaPerdidaEntity.class);
            em.persist(entity);
            
            data.add(entity);
        }
        MascotaPerdidaEntity e = data.get(2);
        MultimediaEntity entity = fac.manufacturePojo(MultimediaEntity.class);
        entity.setMascotaPerdida(e);
        em.persist(entity);
        e.setMultimedia(new ArrayList<>());
        e.getMultimedia().add(entity);
        
        UsuarioEntity entityU = fac.manufacturePojo(UsuarioEntity.class);
        entityU.setMascotasPerdidas(new ArrayList<>());
        entityU.getMascotasPerdidas().add(e);
        em.persist(entityU);
        e.setUsuario(entityU);
        
        
        RecompensaEntity entityR = fac.manufacturePojo(RecompensaEntity.class);
        entityR.setMascotaPerdida(e);
        em.persist(entity);
        e.setRecompensa(entityR);
      
        
    }
    @Test 
    public void createMascotaPerdida ( ) throws BusinessLogicException
    {
        MascotaPerdidaEntity entidad = fac.manufacturePojo(MascotaPerdidaEntity.class);

        MascotaPerdidaEntity result = mascotaLogic.createMascotaPerdida(entidad);
        
        MascotaPerdidaEntity entity = em.find(MascotaPerdidaEntity.class, result.getId());
  
        Assert.assertNotNull(result);
        
        Assert.assertFalse(entidad.equals(null));
        String test = "test";
        Assert.assertFalse(entidad.equals(test));
        MascotaPerdidaEntity met = new MascotaPerdidaEntity();
        met.setId(entidad.getId()+1);
        met.setDescripcion(entidad.getDescripcion());
        met.setEspecie(entidad.getEspecie());
        met.setLugar(entidad.getLugar());
        met.setFechaPerdida(entidad.getFechaPerdida());
        met.setRaza(entidad.getRaza());
        met.setMultimedia(entidad.getMultimedia());
        met.setUsuario(entidad.getUsuario());
        met.setRecompensa(entidad.getRecompensa());
        Assert.assertFalse( entidad.equals(met) );
        met.setId(entidad.getId());
        met.setDescripcion(met.getDescripcion()+"Chao");
        Assert.assertFalse( entidad.equals(met) );
        met.setDescripcion(entidad.getDescripcion());
        Assert.assertTrue(entidad.getDescripcion().equals(met.getDescripcion()));
        
    }
    
    @Test (expected = BusinessLogicException.class)
    public void createMascotaPerdidaRazaNull () throws BusinessLogicException
    {
        MascotaPerdidaEntity newEntity = fac.manufacturePojo(MascotaPerdidaEntity.class);
        
        newEntity.setRaza(null);
        MascotaPerdidaEntity result = mascotaLogic.createMascotaPerdida(newEntity);
    }
    
    @Test (expected = BusinessLogicException.class)
    public void createMascotaPerdidaRazaCadenaVacia () throws BusinessLogicException
    {
        MascotaPerdidaEntity newEntity = fac.manufacturePojo(MascotaPerdidaEntity.class);
        
        newEntity.setRaza("");
        MascotaPerdidaEntity result = mascotaLogic.createMascotaPerdida(newEntity);
    }

    @Test (expected = BusinessLogicException.class)
    public void createMascotaPerdidaEspecieDifetenteGatoPerro () throws BusinessLogicException
    {
        MascotaPerdidaEntity newEntity = fac.manufacturePojo(MascotaPerdidaEntity.class);
        
        newEntity = fac.manufacturePojo(MascotaPerdidaEntity.class);
        Integer i = new Integer ( (int) (Math.random()*TipoEspecies.values().length *50) );
        while ( i>=0 && i<TipoEspecies.values().length)
        {
            i = new Integer ( (int) (Math.random()*TipoEspecies.values().length *50) );
        }
        
        newEntity.setEspecie(i);
        MascotaPerdidaEntity result = mascotaLogic.createMascotaPerdida(newEntity);
    }
    
    @Test (expected = BusinessLogicException.class)
    public void createMascotaPerdidaEspecieNull () throws BusinessLogicException
    {
        MascotaPerdidaEntity newEntity = fac.manufacturePojo(MascotaPerdidaEntity.class);
        
        newEntity.setEspecie(null);
        MascotaPerdidaEntity result = mascotaLogic.createMascotaPerdida(newEntity);
    }
    @Test (expected = BusinessLogicException.class)
    public void createMascotaPerdidaLugarNull () throws BusinessLogicException
    {
        MascotaPerdidaEntity newEntity = fac.manufacturePojo(MascotaPerdidaEntity.class);
        
        newEntity.setLugar(null);
        MascotaPerdidaEntity result = mascotaLogic.createMascotaPerdida(newEntity);
    }
    
    @Test (expected = BusinessLogicException.class)
    public void createMascotaPerdidaLugarCadenaVacia () throws BusinessLogicException
    {
        MascotaPerdidaEntity newEntity = fac.manufacturePojo(MascotaPerdidaEntity.class);
       
        newEntity.setLugar("");
        MascotaPerdidaEntity result = mascotaLogic.createMascotaPerdida(newEntity);
    }
    
    @Test (expected = BusinessLogicException.class)
    public void createMascotaPerdidaDescripcionNull () throws BusinessLogicException
    {
        MascotaPerdidaEntity newEntity = fac.manufacturePojo(MascotaPerdidaEntity.class);
       
        newEntity.setDescripcion(null);
        MascotaPerdidaEntity result = mascotaLogic.createMascotaPerdida(newEntity);
    }
    
    @Test (expected = BusinessLogicException.class)
    public void createMascotaPerdidaDescripcionCadenaVacia () throws BusinessLogicException
    {
        MascotaPerdidaEntity newEntity = fac.manufacturePojo(MascotaPerdidaEntity.class);
       
        newEntity.setDescripcion("");
        MascotaPerdidaEntity result = mascotaLogic.createMascotaPerdida(newEntity);
    }
    
    @Test 
    public void updateMascotaPerdida ( ) throws BusinessLogicException
    {
        MascotaPerdidaEntity entity = fac.manufacturePojo(MascotaPerdidaEntity.class);
        MascotaPerdidaEntity newEntity = fac.manufacturePojo(MascotaPerdidaEntity.class);
        
        mascotaLogic.createMascotaPerdida(entity);
        newEntity.setId(entity.getId());
         
        MascotaPerdidaEntity result = mascotaLogic.updateMascotaPerdida(newEntity);
        
  
         Assert.assertEquals(newEntity, result);
        
    }
    @Test 
    public void updateMascotaPerdidaUsuario ( ) throws BusinessLogicException
    {
        MascotaPerdidaEntity entity = fac.manufacturePojo(MascotaPerdidaEntity.class);
        MascotaPerdidaEntity newEntity = fac.manufacturePojo(MascotaPerdidaEntity.class);
        
        mascotaLogic.createMascotaPerdida(entity);
        newEntity.setId(entity.getId());
        newEntity.setUsuario(null);
         
        MascotaPerdidaEntity result = mascotaLogic.updateMascotaPerdida(newEntity);
        
  
         Assert.assertEquals(newEntity, result);
        
    }
    
    @Test (expected = BusinessLogicException.class)
    public void updateMascotaPerdidaRazaNull () throws BusinessLogicException
    {
        MascotaPerdidaEntity entity = fac.manufacturePojo(MascotaPerdidaEntity.class);
        MascotaPerdidaEntity newEntity = fac.manufacturePojo(MascotaPerdidaEntity.class);
        
        mascotaLogic.createMascotaPerdida(entity);
        
        newEntity.setRaza(null);
        newEntity.setId(entity.getId());
         
        MascotaPerdidaEntity result = mascotaLogic.updateMascotaPerdida(newEntity);
    }
    
    @Test (expected = BusinessLogicException.class)
    public void updateMascotaPerdidaRazaCadenaVacia () throws BusinessLogicException
    {
        MascotaPerdidaEntity entity = fac.manufacturePojo(MascotaPerdidaEntity.class);
        MascotaPerdidaEntity newEntity = fac.manufacturePojo(MascotaPerdidaEntity.class);
        newEntity.setId(entity.getId());
        
        mascotaLogic.createMascotaPerdida(entity);
        
        newEntity.setRaza("");
        MascotaPerdidaEntity result = mascotaLogic.updateMascotaPerdida(newEntity);
    }

    @Test (expected = BusinessLogicException.class)
    public void updateMascotaPerdidaEspecieDifetenteGatoPerro () throws BusinessLogicException
    {
        MascotaPerdidaEntity entity = fac.manufacturePojo(MascotaPerdidaEntity.class);
        MascotaPerdidaEntity newEntity = fac.manufacturePojo(MascotaPerdidaEntity.class);
        newEntity.setId(entity.getId());
        
        mascotaLogic.createMascotaPerdida(entity);
        
        newEntity = fac.manufacturePojo(MascotaPerdidaEntity.class);
        Integer i = new Integer ( (int) (Math.random()*TipoEspecies.values().length *50) );
        while ( i>=0 && i<TipoEspecies.values().length)
        {
            i = new Integer ( (int) (Math.random()*TipoEspecies.values().length *50) );
        }
        
        newEntity.setEspecie(i);
        MascotaPerdidaEntity result = mascotaLogic.updateMascotaPerdida(newEntity);
    }
    
    @Test (expected = BusinessLogicException.class)
    public void updateMascotaPerdidaEspecieNull () throws BusinessLogicException
    {
        MascotaPerdidaEntity entity = fac.manufacturePojo(MascotaPerdidaEntity.class);
        MascotaPerdidaEntity newEntity = fac.manufacturePojo(MascotaPerdidaEntity.class);
        newEntity.setId(entity.getId());
        
        mascotaLogic.createMascotaPerdida(entity);
        
        newEntity.setEspecie(null);
        MascotaPerdidaEntity result = mascotaLogic.updateMascotaPerdida(newEntity);
    }
    @Test (expected = BusinessLogicException.class)
    public void updateMascotaPerdidaLugarNull () throws BusinessLogicException
    {
      MascotaPerdidaEntity entity = fac.manufacturePojo(MascotaPerdidaEntity.class);
        MascotaPerdidaEntity newEntity = fac.manufacturePojo(MascotaPerdidaEntity.class);
        newEntity.setId(entity.getId());
        
        mascotaLogic.createMascotaPerdida(entity);
        
        newEntity.setLugar(null);
        MascotaPerdidaEntity result = mascotaLogic.updateMascotaPerdida(newEntity);
    }
    
    @Test (expected = BusinessLogicException.class)
    public void updateMascotaPerdidaLugarCadenaVacia () throws BusinessLogicException
    {
        MascotaPerdidaEntity entity = fac.manufacturePojo(MascotaPerdidaEntity.class);
        MascotaPerdidaEntity newEntity = fac.manufacturePojo(MascotaPerdidaEntity.class);
        newEntity.setId(entity.getId());
        
        mascotaLogic.createMascotaPerdida(entity);
       
        newEntity.setLugar("");
        MascotaPerdidaEntity result = mascotaLogic.updateMascotaPerdida(newEntity);
    }
    
    @Test (expected = BusinessLogicException.class)
    public void updateMascotaPerdidaDescripcionNull () throws BusinessLogicException
    {
        MascotaPerdidaEntity entity = fac.manufacturePojo(MascotaPerdidaEntity.class);
        MascotaPerdidaEntity newEntity = fac.manufacturePojo(MascotaPerdidaEntity.class);
        newEntity.setId(entity.getId());
        
        mascotaLogic.createMascotaPerdida(entity);
       
        newEntity.setDescripcion(null);
        MascotaPerdidaEntity result = mascotaLogic.updateMascotaPerdida(newEntity);
    }
    
    @Test (expected = BusinessLogicException.class)
    public void updateMascotaPerdidaDescripcionCadenaVacia () throws BusinessLogicException
    {
        MascotaPerdidaEntity entity = fac.manufacturePojo(MascotaPerdidaEntity.class);
        MascotaPerdidaEntity newEntity = fac.manufacturePojo(MascotaPerdidaEntity.class);
        newEntity.setId(entity.getId());
        
        mascotaLogic.createMascotaPerdida(entity);
       
        newEntity.setDescripcion("");
        MascotaPerdidaEntity result = mascotaLogic.updateMascotaPerdida(newEntity);
    }
    
     @Test
    public void deleteTest() throws BusinessLogicException
    {
        PodamFactory factory = new PodamFactoryImpl();
        MascotaPerdidaEntity p = factory.manufacturePojo(MascotaPerdidaEntity.class);
        mascotaLogic.createMascotaPerdida(p);
        mascotaLogic.deleteMascotaPerdida(p.getId());

        Assert.assertNull(em.find(MascotaPerdidaEntity.class,p.getId()));
    }
    
    @Test
    public void findAllTest() throws BusinessLogicException
    {
        PodamFactory factory = new PodamFactoryImpl();

        ArrayList< MascotaPerdidaEntity> resultados = new ArrayList();

        int j = (int) (Math.random() * ((100 - 1) + 1)) + 1;
        for (int i = 0; i <= j; i++) {
            MascotaPerdidaEntity mascota = factory.manufacturePojo(MascotaPerdidaEntity.class);
            mascotaLogic.createMascotaPerdida(mascota);
            Assert.assertNotNull(mascota);
            resultados.add(mascota);
        }

        List<MascotaPerdidaEntity> r = mascotaLogic.getMascotasPerdida();
        Iterator iter = resultados.iterator();

        while (iter.hasNext()) {
            MascotaPerdidaEntity next = (MascotaPerdidaEntity) iter.next();
            Assert.assertTrue(r.contains(next));
        }
    }
    
    @Test
    public void findTest() throws BusinessLogicException
    {
        PodamFactory factory = new PodamFactoryImpl();

        MascotaPerdidaEntity mascota = factory.manufacturePojo(MascotaPerdidaEntity.class);
        mascotaLogic.createMascotaPerdida(mascota);
        Assert.assertNotNull(mascota);
        MascotaPerdidaEntity r = mascotaLogic.getMascotaPerdida(mascota.getId());
        Assert.assertNotNull(r);
    }
   
}
