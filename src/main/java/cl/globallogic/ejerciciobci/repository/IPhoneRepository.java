package cl.globallogic.ejerciciobci.repository;

import cl.globallogic.ejerciciobci.entity.Phone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPhoneRepository extends JpaRepository<Phone, Long> {

}
