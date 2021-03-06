/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.mascotas.ejb;

import co.edu.uniandes.csw.mascotas.entities.ProcesoAdopcionEntity;
import co.edu.uniandes.csw.mascotas.exceptions.BusinessLogicException;
import co.edu.uniandes.csw.mascotas.persistence.ProcesoAdopcionPersistence;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author William Smith
 */
@Stateless
public class ProcesoAdopcionLogic {

    @Inject
    private ProcesoAdopcionPersistence persistence;

    private void ck(ProcesoAdopcionEntity proceso) throws BusinessLogicException {
        if (proceso.getEstado() == null) {
            throw new BusinessLogicException("El estado del proceso se encuentra vacio");
        }
        if (proceso.getComentario() == null) {
            throw new BusinessLogicException("El comentario del proceso se encuentra vacio");
        }
        if (proceso.getCalificacion() < 1 || proceso.getCalificacion() > 5) {
            throw new BusinessLogicException("La calificacion del proceso tiene un valor que no se encuentra entre 1 y 5");
        }
        Boolean validState = false;
        if (proceso.getEstado().equals("En Proceso") || proceso.getEstado().equals("Terminado") || proceso.getEstado().equals("Cancelado")) {
            validState = true;
        }
        if (!validState) {
            throw new BusinessLogicException("El estado del proceso no es un estado valido");
        }
    }

    public ProcesoAdopcionEntity createProcesoAdopcion(ProcesoAdopcionEntity proceso) throws BusinessLogicException {

        ck(proceso);
        proceso = persistence.create(proceso);
        return proceso;
    }

    public ProcesoAdopcionEntity updateProcesoAdopcion(ProcesoAdopcionEntity proceso) throws BusinessLogicException {

        ck(proceso);
        proceso = persistence.update(proceso);
        return proceso;
    }

    public void deleteProcesoAdopcion(Long procesoID) {
        persistence.delete(procesoID);
    }

    public ProcesoAdopcionEntity findProcesoAdopcion(Long procesoID) {
        return persistence.find(procesoID);
    }

    public List<ProcesoAdopcionEntity> findAllProcesosAdopcion() {
        return persistence.findAll();
    }

}
