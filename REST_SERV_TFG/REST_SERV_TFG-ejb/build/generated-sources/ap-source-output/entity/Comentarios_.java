package entity;

import entity.Articulo;
import entity.Usuario;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-07-11T19:29:42")
@StaticMetamodel(Comentarios.class)
public class Comentarios_ { 

    public static volatile SingularAttribute<Comentarios, String> texto;
    public static volatile SingularAttribute<Comentarios, Articulo> idArticulo;
    public static volatile SingularAttribute<Comentarios, Date> fecha;
    public static volatile SingularAttribute<Comentarios, Usuario> idUsuario;
    public static volatile SingularAttribute<Comentarios, Integer> id;
    public static volatile SingularAttribute<Comentarios, Integer> visto;

}