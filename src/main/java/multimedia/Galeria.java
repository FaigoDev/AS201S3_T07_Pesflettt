package multimedia;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named(value = "galeria")
@RequestScoped
public class Galeria implements Serializable {

    private List<String> imagenes;

    @PostConstruct
    public void iniciar() {
        imagenes = new ArrayList<>();
        for (int i = 1; i <= 6; i++) {
            imagenes.add("image" + i + ".png");
        }
    }

    public List<String> getImagenes() {
        return imagenes;
    
}
}
