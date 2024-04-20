package view.uber;

import modeles.uber.Voiture;
import java.util.List;
public interface IVoiture {


   Voiture findById(int id);
        List<Voiture> findAll();
        void save(Voiture voiture);
        void update(Voiture voiture);
        void delete(int id);
    }


