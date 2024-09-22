package mk.ukim.finki.wp.kol2023.g1.repository;

import mk.ukim.finki.wp.kol2023.g1.model.Player;
import mk.ukim.finki.wp.kol2023.g1.model.PlayerPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.Position;
import java.util.List;

@Repository
public interface PlayerRepository  extends JpaRepository<Player, Long> {

    List<Player> findByPosition(PlayerPosition position);
    List<Player> findByPointsPerGameLessThan(Double points);

    List<Player> findByPointsPerGameLessThanAndPosition(Double points, PlayerPosition playerPosition);
}

