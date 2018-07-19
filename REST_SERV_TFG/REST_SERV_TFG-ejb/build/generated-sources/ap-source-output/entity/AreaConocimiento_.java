package entity;

import entity.Articulo;
import entity.CampoEstudio;
import entity.Usuario;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-07-11T19:29:41")
@StaticMetamodel(AreaConocimiento.class)
public class AreaConocimiento_ { 

    public static volatile ListAttribute<AreaConocimiento, Articulo> articuloList;
    public static volatile SingularAttribute<AreaConocimiento, CampoEstudio> campoEstudioId;
    public static volatile ListAttribute<AreaConocimiento, Usuario> usuarioList;
    public static volatile SingularAttribute<AreaConocimiento, Integer> id;
    public static volatile SingularAttribute<AreaConocimiento, String> nombre;

}