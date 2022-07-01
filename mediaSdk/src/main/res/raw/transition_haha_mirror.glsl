precision mediump float;
varying vec2 textureCoordinate;
uniform float progress;
uniform sampler2D vTexture;
uniform sampler2D vTexture1;

void main(){
    float x =smoothstep(.0,1.0,(progress*2.0+textureCoordinate.x-1.0));
    gl_FragColor= mix(texture2D(vTexture1, (textureCoordinate-.5)*(1.-x)+.5), texture2D(vTexture, (textureCoordinate-.5)*x+.5), x);
}