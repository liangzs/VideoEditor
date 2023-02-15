#version 100
precision mediump float;
#define pi 3.14159265358;

varying float maxZaxis;//0
varying vec2 scale;//1
uniform sampler2D vTexture;//4
varying float progress;//5
varying vec2 resolution;//6
varying float right;

void main() {
    vec2 uv = vec2(gl_FragCoord.xy/resolution.xy);

    if (right > 0.5) {
             uv.x -= 1.0;
         uv.x *= -1.0;
    }

    uv = (uv-0.5);
    float iTime =  progress;
    float z = iTime* maxZaxis;
    if (uv.x > 0.0) {
        uv = uv * (1.0+z);
        if (0.5 < uv.x || uv.y < -0.5 || 0.5 < uv.y) {
            gl_FragColor = vec4(0.0);
            return;
        }

    } else {
        vec2 cy = vec2(0.0, uv.y);
        float x = uv.x * z * sqrt(1.0 - 4.0 * uv.x * uv.x + 4.0 * uv.x * uv.x * z * z) + uv.x;
        x = x / (4.0 * uv.x * z * z * uv.x + 1.0);
        cy.x = asin(x/0.5)/pi;

        cy.y = uv.y + uv.y * (x - uv.x)/uv.x;
        if (cy.y < -0.5 || 0.5 < cy.y) {
            gl_FragColor = vec4(0.0);
            return;
        } else {
            uv = cy;
        }
    }


    if (abs(uv.y) <= scale.y * 0.5) {
        uv.y /= scale.y;
    } else {
        gl_FragColor = vec4(0.0);
        return;
    }
    uv.y += 0.5;
    float deltaX = iTime * (0.5 + scale.x * 0.5);
    uv.x += deltaX;
    if (abs(uv.x) <= scale.x * 0.5) {
        uv.x /= scale.x;
    } else {
        gl_FragColor = vec4(0.0);
        return;
    }
    uv.x += 0.5;
    if (uv.x > 1.0) {
        gl_FragColor = vec4(0.0);
        return;
    }

    if (right > 0.5) {
        uv.x -= 1.0;
        uv.x *= -1.0;
    }


    gl_FragColor = texture2D(vTexture, uv);

}