% File name: bmp_to_map.m
% Author: Ryan Lei
% Starting Date: 2010/01/20
% Modification: XXXX/XX/XX
% Description: 

% This MALTAB program transforms a map image file into a textfile 
% "almost readable" by class PlayGame.
%
% Before running this program, be careful about "How you created the map image":
% 1. It should be of png format and transparent in res/drawable.
% 2. You should construct the map.png using only TWO colors:
%    CYAN (0,255,255) for obstacles,
%    RED (255,0,0) for the doors to be open.
% 3. Copy and convert it into map.bmp.
% 4. Use commands like bmp_to_map('map.bmp', 'map.txt') to run the program. 
% 5. YOU MAY STILL HAVE TO MANUALLY MODIFY THE ERRORS AFTERWARD.

% Map symbols:
% 0: nothing    (transparent or WHITE)
% 1: obsticles  (CYAN)
% 2: door1      (RED)
% 3: door2      (RED), and so on. Should be manually edited (search & replace)

function bmp_to_map(src, dest)
    img = double(imread(src, 'bmp'));
    fout = fopen(dest, 'w');
    % Convert it into grayscale so it's more accurate
    img = (img(:,:,1) + img(:,:,2) + img(:,:,3)) / 3;
    
    H = size(img, 1);
    W = size(img, 2);

    % On my computer: RED ~= 94, CYAN ~= 206
    for row = 1 : H
        for col = 1 : W
            % Determmine 'nothing'
            if img(row, col) > 250
                fprintf(fout, '0');
            % Determine 'obsticles'
            elseif img(row, col) > 190
                fprintf(fout, '1');
            % Determine 'doors'
            elseif img(row, col) > 80
                fprintf(fout, '2');
            % Exception
            else
                disp(sprintf('Color error at (%g, %g): Got (%g, %g, %g)', ...
                    row, col, img(row, col, 1), img(row, col, 2), img(row, col, 3)));
                fprintf(fout, '*');
                return;
            end
        end
        fprintf(fout, '\n');
    end
    
    fclose(fout);
    disp('done');
end