package com.itt.atm.repository;

import com.itt.atm.entity.Atm;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Repository
public class AtmRepository {

    private static final Map<UUID, Atm> atms = new HashMap<>();

    public Atm findAtm(String atmId) {
        return atms.get(UUID.fromString(atmId));
    }

    public void updateAtm(Atm atm) {
        atms.put(atm.getId(), atm);
    }

}
