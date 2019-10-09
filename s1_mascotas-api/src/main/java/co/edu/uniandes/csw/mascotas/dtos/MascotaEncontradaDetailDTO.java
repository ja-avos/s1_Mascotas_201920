/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.mascotas.dtos;

import co.edu.uniandes.csw.mascotas.entities.MascotaEncontradaEntity;
import co.edu.uniandes.csw.mascotas.entities.MultimediaEntity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ja.avelino
 */
public class MascotaEncontradaDetailDTO  extends MascotaEncontradaDTO implements Serializable{
       private List<MultimediaDTO> fotos;
         public MascotaEncontradaDetailDTO() {
        
    }
    
     public MascotaEncontradaDetailDTO(MascotaEncontradaEntity mascotaEntity) {
        super(mascotaEntity);
       
            if (mascotaEntity.getFotos() != null) {
                fotos = new ArrayList<>();
                for (MultimediaEntity fotoEntity : mascotaEntity.getFotos()) {
                   fotos.add(new MultimediaDTO(fotoEntity));
                }
            }
        }
  

    /**
     * Transformar un DTO a un Entity
     *
     * @return El DTO de la editorial para transformar a Entity
     */
    @Override
    public MascotaEncontradaEntity toEntity() {
        MascotaEncontradaEntity mascotaEntity = super.toEntity();
       
         if (getFotos() != null) {
            List<MultimediaEntity> multimediaEntity = new ArrayList<>();
            for (MultimediaDTO dtoFoto : getFotos()) {
                multimediaEntity.add(dtoFoto.toEntity());
            }
            mascotaEntity.setFotos(multimediaEntity);
        }
        return mascotaEntity;
    }
    /**
     * @return the fotos
     */
    public List<MultimediaDTO> getFotos() {
        return fotos;
    }

    /**
     * @param fotos the fotos to set
     */
    public void setFotos(List<MultimediaDTO> fotos) {
        this.fotos = fotos;
    }
}