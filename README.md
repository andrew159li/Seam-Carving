# Seam-Carving

Implementing a content-aware image resizing algorithm.

# Seam Carving Algorithm

# Background
Read through the description of the seam carving algorithm.

Seam-carving is a content-aware image resizing technique where the image is reduced in size by one pixel of height (or width) at a time. A vertical seam in an image is a path of adjacent or diagonally-adjacent pixels connected from the top of the image to the bottom, with one pixel in each row; a horizontal seam is the same from left to right. (Seams cannot wrap around the image: e.g., a vertical seam cannot cross from the leftmost column of the image to the rightmost column.) Below left is the original 505-by-287 pixel image; below right is the result after removing 150 vertical seams, resulting in a 30% narrower image. Unlike standard content-agnostic resizing techniques (such as cropping and scaling), seam carving preserves the most interest features (aspect ratio, set of objects present, etc.) of the image.

Although the underlying algorithm is simple and elegant, it was not discovered until 2007. Now, it is now a core feature in Adobe Photoshop and other computer graphics applications.

# Finding Seams

After calculating the energy of each pixel, the resizing algorithm needs to find the minimum-energy seam. To recap, a seam is a path of adjacent or diagonally-adjacent pixels from one side of the image to the other, with exactly 1 pixel from each row/column, for vertical/horizontal seams respectively. (Opposite sides of an image are not adjacent, so seams cannot wrap around the image.)
