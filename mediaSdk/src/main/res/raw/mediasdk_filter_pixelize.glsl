precision mediump float;

uniform vec3                iResolution;
uniform sampler2D           inputImageTexture;
varying vec2                textureCoordinate;
uniform float strength;
#define S (iResolution.x / 6e1) // The cell size.

void mainImage(out vec4 fragColor, vec2 p)
{
    vec4 originColor = texture2D(inputImageTexture, textureCoordinate);
    vec4 resultColor = texture2D(inputImageTexture, floor((p + .5) / S) * S / iResolution.xy);
    fragColor =mix(originColor, resultColor,strength);

}

void main() {
	mainImage(gl_FragColor, textureCoordinate*iResolution.xy);
}