precision mediump float;

uniform vec3                iResolution;
uniform sampler2D           inputImageTexture;
varying vec2                textureCoordinate;

#define S (iResolution.x / 6e1) // The cell size.

void mainImage(out vec4 c, vec2 p)
{
    c = texture2D(inputImageTexture, floor((p + .5) / S) * S / iResolution.xy);
}

void main() {
	mainImage(gl_FragColor, textureCoordinate*iResolution.xy);
}