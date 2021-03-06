/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.mascotas.ejb;

import co.edu.uniandes.csw.mascotas.entities.MascotaEncontradaEntity;
import co.edu.uniandes.csw.mascotas.entities.UsuarioEntity;
import co.edu.uniandes.csw.mascotas.exceptions.BusinessLogicException;
import co.edu.uniandes.csw.mascotas.persistence.MascotaEncontradaPersistence;
import co.edu.uniandes.csw.mascotas.persistence.UsuarioPersistence;
import java.util.List;
import javax.inject.Inject;

/**
 *
 * @author Estudiante
 */
public class UsuarioMascotasEncontradasLogic {
            
        @Inject
        private UsuarioPersistence usuarioPersistence;
        
        @Inject
        private MascotaEncontradaPersistence mascotaPersistence;
        
        public MascotaEncontradaEntity addMascota(Long mascotaId, Long usuarioId) {
            UsuarioEntity usuarioEntity = usuarioPersistence.find(usuarioId);
            MascotaEncontradaEntity mascotaEntity = mascotaPersistence.find(mascotaId);
            mascotaEntity.setUsuario(usuarioEntity);
            return mascotaEntity;
        }
        
        public List<MascotaEncontradaEntity> getMascotas(Long usuarioId) {
            return usuarioPersistence.find(usuarioId).getMascotasEncontradas();
        }
        
        public MascotaEncontradaEntity getMascota(Long usuarioId, Long mascotaId) throws BusinessLogicException {
            List<MascotaEncontradaEntity> mascotas = usuarioPersistence.find(usuarioId).getMascotasEncontradas();
            MascotaEncontradaEntity mascotaEntity = mascotaPersistence.find(mascotaId);
            int index = mascotas.indexOf(mascotaEntity);
            if (index >= 0) {
                return mascotas.get(index);
            }
            throw new BusinessLogicException("La mascota no está asociada al usuario");
        }
        
        public List<MascotaEncontradaEntity> replaceMascotas(Long usuarioId, List<MascotaEncontradaEntity> mascotas) {
            UsuarioEntity usuarioEntity = usuarioPersistence.find(usuarioId);
            List<MascotaEncontradaEntity> mList = mascotaPersistence.findAll();
            for (MascotaEncontradaEntity masc : mList) {
                if (mascotas.contains(masc)) {
                    masc.setUsuario(usuarioEntity);
                } else if (masc.getUsuario() != null && masc.getUsuario().equals(usuarioEntity)) {
                    masc.setUsuario(null);
                }
            }
            return mascotas;
        }
}
