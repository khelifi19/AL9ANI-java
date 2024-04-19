package view;

import modeles.Chauffeur;
import java.util.List;

public interface IChauffeur {

    Chauffeur findById(int id);

    List<Chauffeur> findAll();

    void save(Chauffeur chauffeur);

    void update(Chauffeur chauffeur);

    void delete(int id);
}

