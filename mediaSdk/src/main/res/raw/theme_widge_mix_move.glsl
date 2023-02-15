#version 100
precision mediump float;
varying vec2 textureCoordinate;
uniform float progress;
uniform sampler2D vTexture;
//uniform vec2 direction;
void main(){
    vec2 direction=vec2(0.0,-1.0);
    vec2 p = textureCoordinate +  progress*direction*0.1;
    float m =step(0.0, p.y) * step(p.y, 1.0) * step(0.0, p.x) * step(p.x, 1.0);
    vec4 c2 = vec4(0,0,0,0.0);
    vec4 color = mix(texture2D(vTexture, textureCoordinate), c2, m);
    gl_FragColor = vec4(color);
    //gl_FragColor=texture2D(vTexture, textureCoordinate);
}