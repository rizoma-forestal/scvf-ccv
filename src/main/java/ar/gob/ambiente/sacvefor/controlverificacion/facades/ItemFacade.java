/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.gob.ambiente.sacvefor.controlverificacion.facades;

import ar.gob.ambiente.sacvefor.controlverificacion.entities.Item;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author rincostante
 */
@Stateless
public class ItemFacade extends AbstractFacade<Item> {

    @PersistenceContext(unitName = "sacvefor-controlVerificacionPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ItemFacade() {
        super(Item.class);
    }
    
}
