precision mediump float;
varying vec2 textureCoordinate;
uniform float progress;
uniform float alpha;
uniform sampler2D vTexture;
uniform sampler2D vTexture1;


vec2 offset(float progress, float x, float theta) {
  float phase = progress*progress + progress + theta;
  float shifty = 0.03*progress*cos(10.0*(progress+x));
  return vec2(0, shifty);
}
void main(){
    vec4 color = mix(texture2D(vTexture1, textureCoordinate+offset(progress,textureCoordinate.x,0.0)),
    texture2D(vTexture, textureCoordinate+offset(1.0-progress,textureCoordinate.x,3.14)), progress);
    gl_FragColor = vec4(color)*alpha;
}