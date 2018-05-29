package org.geigercounter.jsf;

import org.geigercounter.entity.Hardware;
import org.geigercounter.jsf.util.JsfUtil;
import org.geigercounter.jsf.util.JsfUtil.PersistAction;
import org.geigercounter.sessionbean.HardwareFacade;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@Named("hardwareController")
@SessionScoped
public class HardwareController implements Serializable {

    @EJB
    private org.geigercounter.sessionbean.HardwareFacade ejbFacade;
    private List<Hardware> items = null;
    private Hardware selected;

    public HardwareController() {
    }

    public Hardware getSelected() {
        return selected;
    }

    public void setSelected(Hardware selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private HardwareFacade getFacade() {
        return ejbFacade;
    }

    public Hardware prepareCreate() {
        selected = new Hardware();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/bundle").getString("HardwareCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/bundle").getString("HardwareUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/bundle").getString("HardwareDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Hardware> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(selected);
                } else {
                    getFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/bundle").getString("PersistenceErrorOccured"));
            }
        }
    }

    public Hardware getHardware(java.lang.Short id) {
        return getFacade().find(id);
    }

    public List<Hardware> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Hardware> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Hardware.class)
    public static class HardwareControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            HardwareController controller = (HardwareController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "hardwareController");
            return controller.getHardware(getKey(value));
        }

        java.lang.Short getKey(String value) {
            java.lang.Short key;
            key = Short.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Short value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Hardware) {
                Hardware o = (Hardware) object;
                return getStringKey(o.getHardwareid());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Hardware.class.getName()});
                return null;
            }
        }

    }

}
