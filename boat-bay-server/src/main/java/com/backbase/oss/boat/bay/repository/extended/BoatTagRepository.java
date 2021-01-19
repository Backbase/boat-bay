package com.backbase.oss.boat.bay.repository.extended;

import com.backbase.oss.boat.bay.domain.Tag;
import com.backbase.oss.boat.bay.repository.TagRepository;
import java.util.Optional;

public interface BoatTagRepository extends TagRepository {

    Optional<Tag> findByName(String name);

}
