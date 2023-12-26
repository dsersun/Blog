package org.sersun.simpleblog.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RoleID {
    MAIN_ADMIN(1), //Level GOD :)
    ADMIN(2),
    MODERATOR(3),
    READER(4),
    BLOGGER(5),
    BLOCKED_READER(6), // temporary block
    BLOCKED_BLOGGER(7), // temporary block
    BLACKLIST(8); // permanent block

    private final int id;
    }
