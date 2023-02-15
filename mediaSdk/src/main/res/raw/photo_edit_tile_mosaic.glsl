varying highp vec2 textureCoordinate;
precision highp float;

uniform vec3 iResolution;
uniform sampler2D inputImageTexture;
uniform float strength;

void mainImage(out vec4 fragColor, in vec2 fragCoord)
{
    float textureSamplesCount = 3.0;
    float textureEdgeOffset = 0.005;
    float borderSize = 1.0;

    float tileSize = 17.0;
    vec2 tileNumber = floor(fragCoord / tileSize);
    vec4 oriColor= texture2D(inputImageTexture, textureCoordinate);
    float resultAlpha=oriColor.a*(1.0/(oriColor.a+0.00001));
    vec4 accumulator = vec4(0.0);
    for (float y = 0.0; y < textureSamplesCount; ++y)
    {
        for (float x = 0.0; x < textureSamplesCount; ++x)
        {
            vec2 textureCoordinates = (tileNumber + vec2((x + 0.5)/textureSamplesCount, (y + 0.5)/textureSamplesCount)) * tileSize / iResolution.xy;
            textureCoordinates = clamp(textureCoordinates, 0.0 + textureEdgeOffset, 1.0 - textureEdgeOffset);
            accumulator += texture2D(inputImageTexture, textureCoordinates);
        }
    }

    fragColor = accumulator / vec4(textureSamplesCount * textureSamplesCount);

    vec2 pixelNumber = floor(fragCoord - (tileNumber * tileSize));
    pixelNumber = mod(pixelNumber + borderSize, tileSize);

    float pixelBorder = step(min(pixelNumber.x, pixelNumber.y), borderSize) * step(borderSize * 2.0 + 1.0, tileSize);

    fragColor *= pow(fragColor, vec4(pixelBorder));
    fragColor = mix(oriColor, fragColor, strength);
}

void main() {
    mainImage(gl_FragColor, textureCoordinate.xy*iResolution.xy);
}