package org.geigercounter.jsf;

import org.geigercounter.entity.Cpm;
import org.geigercounter.jsf.util.JsfUtil;
import org.geigercounter.jsf.util.JsfUtil.PersistAction;
import org.geigercounter.sessionbean.CpmFacade;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
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

@Named("cpmController")
@SessionScoped
public class CpmController implements Serializable {

    @EJB
    private org.geigercounter.sessionbean.CpmFacade ejbFacade;
    private List<Cpm> items = null;
    private Cpm selected;

    public CpmController() {
    }

    public Cpm getSelected() {
        return selected;
    }

    public void setSelected(Cpm selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
        selected.setCpmPK(new org.geigercounter.entity.CpmPK());
    }

    private CpmFacade getFacade() {
        return ejbFacade;
    }

    public Cpm prepareCreate() {
        selected = new Cpm();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/bundle").getString("CpmCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/bundle").getString("CpmUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/bundle").getString("CpmDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Cpm> getItems() {
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

    public Cpm getCpm(org.geigercounter.entity.CpmPK id) {
        return getFacade().find(id);
    }

    public List<Cpm> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Cpm> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Cpm.class)
    public static class CpmControllerConverter implements Converter {

        private static final String SEPARATOR = "#";
        private static final String SEPARATOR_ESCAPED = "\\#";

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            CpmController controller = (CpmController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "cpmController");
            return controller.getCpm(getKey(value));
        }

        org.geigercounter.entity.CpmPK getKey(String value) {
            org.geigercounter.entity.CpmPK key;
            String values[] = value.split(SEPARATOR_ESCAPED);
            key = new org.geigercounter.entity.CpmPK();
            key.sethardwareid(Short.parseShort(values[0]));
            key.setTimestamp(LocalDateTime.ofInstant(java.sql.Date.valueOf(values[1]).toInstant(), ZoneOffset.UTC));
            return key;
        }

        String getStringKey(org.geigercounter.entity.CpmPK value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value.gethardwareid());
            sb.append(SEPARATOR);
            sb.append(value.getTimestamp());
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Cpm) {
                Cpm o = (Cpm) object;
                return getStringKey(o.getCpmPK());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Cpm.class.getName()});
                return null;
            }
        }

    }

}
