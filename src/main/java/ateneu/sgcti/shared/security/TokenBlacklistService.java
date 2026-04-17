package ateneu.sgcti.shared.security;

import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenBlacklistService {

    private final Set<String> blacklist = ConcurrentHashMap.newKeySet();

    public void revoke(String token) {
        blacklist.add(token);
    }

    public boolean isRevoked(String token) {
        return blacklist.contains(token);
    }
}
