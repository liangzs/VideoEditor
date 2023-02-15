varying mediump
vec2 textureCoordinate;
precision lowp
float;

uniform vec3
iResolution;
uniform sampler2D
inputImageTexture;
uniform float strength;


#define S (iResolution.x / 6e1)// The cell size.

void mainImage(out vec4 c, vec2 p) {
    float cellSize = S * strength + 1.0;
    c = texture2D(inputImageTexture, floor((p + .5) / cellSize) * cellSize / iResolution.xy);
}

void main() {
    mainImage(gl_FragColor, textureCoordinate * iResolution.xy);
}