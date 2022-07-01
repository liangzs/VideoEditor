precision mediump float;
varying vec2 textureCoordinate;
uniform float progress;
uniform sampler2D vTexture;
uniform sampler2D vTexture1;
float rand (vec2 co) {
    return fract(sin(dot(co.xy ,vec2(12.9898,78.233))) * 437.5453);
}

void main(){
     ivec2 size=ivec2(10.0, 10.0); // = ivec2(10, 10)
     float smoothness=0.5; // = 0.5
     float r = rand(floor(vec2(size) * textureCoordinate));
     float m = smoothstep(0.0, -smoothness, r - (progress * (1.0 + smoothness)));
     gl_FragColor= mix(texture2D(vTexture1, textureCoordinate), texture2D(vTexture, textureCoordinate), m);
}