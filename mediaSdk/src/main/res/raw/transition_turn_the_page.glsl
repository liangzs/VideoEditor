#version 100
#define PI 3.14159265358974
precision mediump float;
varying vec2 textureCoordinate;
uniform float progress;
uniform sampler2D vTexture;
uniform sampler2D vTexture1;


void main(){

    vec2 uv = textureCoordinate;
   float ratio = 1.0;

    uv.xy -=0.5;
    uv.xy/=ratio;

    uv.xy+=0.5;

   vec4 bg = vec4(1., 1., 1., 1.);


    if (progress < 0.5 && uv.x < 0.5) {
        if (uv.x < 0. || uv.y > 1. || uv.y < 0.) {
            gl_FragColor = bg;
        } else {
            gl_FragColor = texture2D(vTexture1, uv);
        }
        return;
    }
    if (progress > 0.5 && uv.x > 0.5) {
        if (uv.x > 1. || uv.y > 1. || uv.y < 0.) {
            gl_FragColor = bg;
        } else {
            gl_FragColor = texture2D(vTexture, uv);
        }
        return;
    }
    float x = cos(progress * PI);
    float ux = 0.5 + 0.5 * x;
    if (ux < 0.5 && uv.x < ux) {
        if (uv.x < 0. || uv.y > 1. || uv.y < 0.) {
            gl_FragColor = bg;
        } else {
            gl_FragColor = texture2D(vTexture1, uv);
        }
        return;
    }
    if (ux > 0.5 && uv.x > ux) {
        if (uv.x > 1. || uv.y > 1. || uv.y < 0.) {
            gl_FragColor = bg;
        } else {
            gl_FragColor = texture2D(vTexture, uv);
        }
        return;

    }
    //progress < 0.5
    if (1.0 >= ux && ux > 0.5) {
        uv.x =(uv.x - 0.5)/x;
        float currentHeight = (1.0 + ((1.0-ux) * 0.4    *uv.x*2.0));
        //uv.x = 0~0.5
        uv.y = (uv.y-0.5)/currentHeight + 0.5;

        uv.x += 0.5;
        if (uv.x < 0.0 || uv.x > 1.0 || uv.y < 0.0 || uv.y > 1.0) {

            gl_FragColor = bg;
            return;
        }

        gl_FragColor = texture2D(vTexture1, uv);

        return;
    }
    if (0.0 <= ux && ux < 0.5) {
        uv.x =  -(0.5-uv.x)/x;
        float currentHeight = (1.0 + ((ux) * 0.4    *uv.x*2.0));

        uv.y = (uv.y-0.5)/currentHeight + 0.5;
        uv.x = 0.5 - uv.x;
        if (uv.x < 0.0 || uv.x > 1.0 || uv.y < 0.0 || uv.y > 1.0) {

            gl_FragColor = bg;
            return;
        }
        gl_FragColor = texture2D(vTexture, uv);



        return;
    }


}