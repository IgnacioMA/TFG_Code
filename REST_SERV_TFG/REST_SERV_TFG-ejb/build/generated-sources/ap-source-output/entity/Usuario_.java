package entity;

import entity.AreaConocimiento;
import entity.Articulo;
import entity.Categoria;
import entity.Comentarios;
import entity.Mensaje;
import entity.Notificaciones;
import entity.Sancion;
import entity.Valoracion;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-07-11T19:29:41")
@StaticMetamodel(Usuario.class)
public class Usuario_ { 

    public static volatile ListAttribute<Usuario, Articulo> articuloList;
    public static volatile ListAttribute<Usuario, Comentarios> comentariosList;
    public static volatile SingularAttribute<Usuario, Integer> idUsuario;
    public static volatile ListAttribute<Usuario, Mensaje> mensajeList;
    public static volatile SingularAttribute<Usuario, Integer> bloqueado;
    public static volatile SingularAttribute<Usuario, Categoria> idRango;
    public static volatile SingularAttribute<Usuario, Sancion> sancionId;
    public static volatile ListAttribute<Usuario, AreaConocimiento> areaConocimientoList;
    public static volatile ListAttribute<Usuario, Articulo> articuloList1;
    public static volatile ListAttribute<Usuario, Notificaciones> notificacionesList;
    public static volatile SingularAttribute<Usuario, String> alias;
    public static volatile SingularAttribute<Usuario, String> contrasena;
    public static volatile ListAttribute<Usuario, Valoracion> valoracionList;
    public static volatile SingularAttribute<Usuario, String> email;

}