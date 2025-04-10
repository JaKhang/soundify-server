package com.soundify.server.shared.data;

import jakarta.persistence.Embeddable;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Objects;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@Embeddable
public class Image {
    // Getter
    private String url;
    private int height;
    private int width;

    // Constructor với kiểm tra giá trị hợp lệ
    public Image(String url, int height, int width) {
        validate(url, height, width);
        this.url = url;
        this.height = height;
        this.width = width;
    }

    // Phương thức validate để kiểm tra giá trị đầu vào
    private void validate(String url, int height, int width) {
        if (url == null || url.trim().isEmpty()) {
            throw new IllegalArgumentException("URL not black.");
        }
        if (height <= 0) {
            throw new IllegalArgumentException("Height greater than 0.");
        }
        if (width <= 0) {
            throw new IllegalArgumentException("Width greater than 0.");
        }
    }


    // Override equals để so sánh các đối tượng Image dựa trên URL
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Image image = (Image) o;
        return Objects.equals(url, image.url);
    }

    // Override hashCode để tạo mã băm dựa trên URL
    @Override
    public int hashCode() {
        return Objects.hashCode(url);
    }

    // Override toString để hiển thị thông tin Image
    @Override
    public String toString() {
        return "Image{" +
                "url='" + url + '\'' +
                ", height=" + height +
                ", width=" + width +
                '}';
    }
}
