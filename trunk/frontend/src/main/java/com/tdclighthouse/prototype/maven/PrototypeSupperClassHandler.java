package com.tdclighthouse.prototype.maven;

/*
 *    Copyright 2013 Smile B.V
 *
 *   Licensed under the Apache License, Version 2.0 (the "License")
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *   
 */

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.mavenhippo.gen.ClassReference;
import net.sourceforge.mavenhippo.gen.ImportRegistry;
import net.sourceforge.mavenhippo.gen.SupperClassHandler;
import net.sourceforge.mavenhippo.gen.annotation.Weight;
import net.sourceforge.mavenhippo.model.ContentTypeBean;
import net.sourceforge.mavenhippo.model.HippoBeanClass;
import net.sourceforge.mavenhippo.utils.Constants;
import net.sourceforge.mavenhippo.utils.NamespaceUtils;
import net.sourceforge.mavenhippo.utils.exceptions.HandlerException;

import org.apache.commons.lang3.StringUtils;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoItem;

import com.tdclighthouse.prototype.beans.TdcDocument;

/**
 * @author Ebrahim Aharpour
 * 
 */
@Weight(value = -10.0)
public class PrototypeSupperClassHandler extends SupperClassHandler {

    private Comparator<Class<? extends HippoBean>> classExtensionComparator = new Comparator<Class<? extends HippoBean>>() {

        @Override
        public int compare(Class<? extends HippoBean> o1, Class<? extends HippoBean> o2) {
            int result = 0;
            if (o1 != null && o2 == null || (o2 != null && o1.isAssignableFrom(o2))) {
                result = 1;
            } else if (o1 == null && o2 != null || (o1 != null && o2.isAssignableFrom(o1))) {
                result = -1;
            }
            return result;
        }
    };

    public PrototypeSupperClassHandler(Map<String, HippoBeanClass> beansOnClassPath,
            Map<String, HippoBeanClass> beansInProject, ClassLoader classLoader, Set<String> namespaces,
            Map<String, ContentTypeBean> mixins) {
        super(beansOnClassPath, beansInProject, classLoader, namespaces, mixins);
    }

    @Override
    public ClassReference getSupperClass(ContentTypeBean contentTypeBean, ImportRegistry importRegistry,
            String packageName) {
        ClassReference result = null;
        if (contentTypeBean.isMixin()) {
            result = new ClassReference(HippoItem.class);
        } else {
            List<String> supertypes = contentTypeBean.getSupertypes();
            result = extendsGeneratedBean(packageName, supertypes);

            if (result == null) {
                result = extendsExistingBeans(supertypes);
            }
            if (result == null) {
                result = new ClassReference(TdcDocument.class);
            }
        }

        importRegistry.register(result);
        return result;
    }

    @SuppressWarnings("unchecked")
    private ClassReference extendsExistingBeans(List<String> supertypes) {
        ClassReference result = null;
        SortedSet<Class<? extends HippoBean>> supperClasses = new TreeSet<Class<? extends HippoBean>>(
                classExtensionComparator);
        for (String superType : supertypes) {
            if (Constants.NodeType.HIPPO_COMPOUND.equals(superType)) {
                Class<TdcDocument> tdcDocumentClass = TdcDocument.class;
                supperClasses.add(tdcDocumentClass);
            } else if (getBeansOnClassPath().containsKey(superType)) {
                HippoBeanClass hippoBeanClass = getBeansOnClassPath().get(superType);
                Class<?> clazz = getClass(hippoBeanClass);
                supperClasses.add((Class<? extends HippoBean>) clazz);
            }
        }
        if (!supperClasses.isEmpty()) {
            result = new ClassReference(supperClasses.last());
        }
        return result;
    }

    private ClassReference extendsGeneratedBean(String packageName, List<String> supertypes) {
        ClassReference result = null;
        for (String superType : supertypes) {
            String ns = NamespaceUtils.getNamespace(superType);
            if (StringUtils.isNotBlank(ns) && getNamespaces().contains(ns) && !getMixins().containsKey(superType)) {
                result = new ClassReference(getClassName(packageName, superType));
                break;
            }
        }
        return result;
    }

    private String getClassName(String packageName, String superType) {
        String result;
        if (StringUtils.isNotBlank(packageName)) {
            result = packageName + Constants.Language.PACKAGE_SEPARATOR + getClassNameHandler().getClassName(superType);
        } else {
            result = getClassNameHandler().getClassName(superType);
        }
        return result;
    }

    private Class<?> getClass(HippoBeanClass hippoBeanClass) {
        try {
            return Class.forName(hippoBeanClass.getFullyQualifiedName(), true, getClassLoader());
        } catch (ClassNotFoundException e) {
            throw new HandlerException(e.getMessage(), e);
        }
    }

}
