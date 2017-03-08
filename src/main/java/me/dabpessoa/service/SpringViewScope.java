package me.dabpessoa.service;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

import javax.faces.context.FacesContext;
import java.util.Map;

public class SpringViewScope implements Scope {

	@Override
    public Object get(String name, ObjectFactory<?> objectFactory) {
		if (FacesContext.getCurrentInstance() == null) {
			return objectFactory.getObject();
		}

        Map<String, Object> viewMap = FacesContext.getCurrentInstance().getViewRoot().getViewMap();
        if(viewMap.containsKey(name)) {
            return viewMap.get(name);
        } else {
            Object object = objectFactory.getObject();
            viewMap.put(name, object);
            return object;
        }
	}

	@Override
	public String getConversationId() {
		return null;
	}

	@Override
	public void registerDestructionCallback(String arg0, Runnable arg1) {
	}

	@Override
	public Object remove(String arg0) {
		return FacesContext.getCurrentInstance().getViewRoot().getViewMap().remove(arg0);
	}

	@Override
	public Object resolveContextualObject(String arg0) {
		return null;
	}

}