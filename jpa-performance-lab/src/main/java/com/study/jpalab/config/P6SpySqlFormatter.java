package com.study.jpalab.config;

import com.p6spy.engine.spy.appender.MessageFormattingStrategy;
import org.hibernate.engine.jdbc.internal.FormatStyle;

public class P6SpySqlFormatter implements MessageFormattingStrategy {

    @Override
    public String formatMessage(
            int connectionId,
            String now,
            long elapsed,
            String category,
            String prepared,
            String sql,
            String url
    ) {
        if (sql == null || sql.isBlank()) {
            return "";
        }

        String formattedSql = FormatStyle.BASIC
                .getFormatter()
                .format(sql);

        return "%d ms | %s%n%s"
                .formatted(elapsed, category, formattedSql);
    }
}