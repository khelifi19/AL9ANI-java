package service.reclamation;

import java.util.List;

public interface IReponse <T>{
    public void ajouter(T t);
    public void supprimer(T t);
    public void modifier(T t);
    public List<T> afficher();

}
