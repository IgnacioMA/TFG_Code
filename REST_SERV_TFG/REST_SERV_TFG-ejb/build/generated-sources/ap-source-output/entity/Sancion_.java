package entity;

import entity.Usuario;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-07-11T19:29:41")
@StaticMetamodel(Sancion.class)
public class Sancion_ { 

    public static volatile SingularAttribute<Sancion, String> tipo;
    public static volatile ListAttribute<Sancion, Usuario> usuarioList;
    public static volatile SingularAttribute<Sancion, Integer> id;

}