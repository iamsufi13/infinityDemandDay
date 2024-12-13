package com.contenttree.admin;

import com.contenttree.utils.ApiResponse1;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Setter
@Getter
public class Widget {
        private Long id;
        private String cardColor;
        private String label;
        private String counter;
        private String link;
        private String bgcolor;
        private String icon;
        private int decimals;

    public Widget(long id, String primary, String totalWhitepapers, ResponseEntity<ApiResponse1<?>> allWhitePaperCounts, String viewAllWhitepapers, String secondary, String icon, int decimals) {
    }

    public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getCardColor() {
            return cardColor;
        }

        public void setCardColor(String cardColor) {
            this.cardColor = cardColor;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getCounter() {
            return counter;
        }

        public void setCounter(String counter) {
            this.counter = counter;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getBgcolor() {
            return bgcolor;
        }

        public void setBgcolor(String bgcolor) {
            this.bgcolor = bgcolor;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public int getDecimals() {
            return decimals;
        }

        public void setDecimals(int decimals) {
            this.decimals = decimals;
        }
}
