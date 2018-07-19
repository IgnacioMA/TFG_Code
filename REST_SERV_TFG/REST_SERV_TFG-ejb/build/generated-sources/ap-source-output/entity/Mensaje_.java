package entity;

import entity.Usuario;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-07-11T19:29:41")
@StaticMetamodel(Mensaje.class)
public class Mensaje_ { 

    public static volatile SingularAttribute<Mensaje, Integer> contadorE;
    public static volatile SingularAttribute<Mensaje, String> contenido;
    public static volatile SingularAttribute<Mensaje, Integer> idReceptor;
    public static volatile SingularAttribute<Mensaje, Integer> contadorR;
    public static volatile SingularAttribute<Mensaje, String> titulo;
    public static volatile SingularAttribute<Mensaje, Integer> leido;
    public static volatile SingularAttribute<Mensaje, Usuario> idEmisor;
    public static volatile SingularAttribute<Mensaje, Integer> id;

}