package entity;

import entity.AreaConocimiento;
import entity.Comentarios;
import entity.Usuario;
import entity.Valoracion;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-07-11T19:29:41")
@StaticMetamodel(Articulo.class)
public class Articulo_ { 

    public static volatile SingularAttribute<Articulo, Integer> idArticulo;
    public static volatile SingularAttribute<Articulo, String> texto;
    public static volatile SingularAttribute<Articulo, Usuario> revisor;
    public static volatile SingularAttribute<Articulo, Date> fecha;
    public static volatile SingularAttribute<Articulo, Usuario> redactor;
    public static volatile ListAttribute<Articulo, Comentarios> comentariosList;
    public static volatile SingularAttribute<Articulo, Integer> subido;
    public static volatile SingularAttribute<Articulo, String> titulo;
    public static volatile SingularAttribute<Articulo, Integer> validado;
    public static volatile ListAttribute<Articulo, AreaConocimiento> areaConocimientoList;
    public static volatile ListAttribute<Articulo, Valoracion> valoracionList;

}