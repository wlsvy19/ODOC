package com.example.board.config;

import com.example.board.shop.product.Product;
import com.example.board.shop.product.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initProducts(ProductRepository productRepository) {
        return args -> {
            if (productRepository.count() > 0) {
                return;
            }

            productRepository.save(new Product(
                    "시그니처 후드",
                    "부드러운 코튼 원단으로 만든 데일리 후드",
                    "의류",
                    69000,
                    18,
                    "https://images.unsplash.com/photo-1556821840-3a63f95609a7?auto=format&fit=crop&w=900&q=80"
            ));
            productRepository.save(new Product(
                    "미니멀 백팩",
                    "노트북 수납과 생활 방수가 가능한 가벼운 백팩",
                    "가방",
                    89000,
                    12,
                    "https://images.unsplash.com/photo-1553062407-98eeb64c6a62?auto=format&fit=crop&w=900&q=80"
            ));
            productRepository.save(new Product(
                    "세라믹 머그",
                    "아침 커피를 오래 따뜻하게 잡아주는 350ml 머그",
                    "리빙",
                    18000,
                    40,
                    "https://images.unsplash.com/photo-1514228742587-6b1558fcca3d?auto=format&fit=crop&w=900&q=80"
            ));
            productRepository.save(new Product(
                    "데스크 램프",
                    "밝기 조절이 가능한 알루미늄 데스크 램프",
                    "리빙",
                    54000,
                    9,
                    "https://images.unsplash.com/photo-1507473885765-e6ed057f782c?auto=format&fit=crop&w=900&q=80"
            ));
        };
    }
}
