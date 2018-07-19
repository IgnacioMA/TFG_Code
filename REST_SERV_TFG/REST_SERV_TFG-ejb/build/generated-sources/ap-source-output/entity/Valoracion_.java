package entity;

import entity.Articulo;
import entity.Usuario;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-07-11T19:29:41")
@StaticMetamodel(Valoracion.class)
public class Valoracion_ { 

    public static volatile SingularAttribute<Valoracion, Articulo> idArticulo;
    public static volatile SingularAttribute<Valoracion, Integer> puntuacion;
    public static volatile SingularAttribute<Valoracion, Usuario> idUsuario;
    public static volatile SingularAttribute<Valoracion, Integer> id;

}