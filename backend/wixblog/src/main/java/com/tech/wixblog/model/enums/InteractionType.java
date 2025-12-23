package com.tech.wixblog.model.enums;

import lombok.Getter;

@Getter
public enum InteractionType {
        VIEW(1),
        LIKE(3),
        SAVE(4),
        SHARE(2),
        COMMENT(5),
        COMPLETE_READ(5);
        
        private final int weight;
        
        InteractionType(int weight) {
            this.weight = weight;
        }

}