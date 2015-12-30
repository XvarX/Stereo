function [img_set] = map(image1, image2)
left_image = rgb2gray(image1);
right_image = rgb2gray(image2);
[Ml,Nl] = size(left_image);
SSD_img = zeros(Ml,Nl);
NCC_img = zeros(Ml,Nl);
for i = 1:Ml
    for j = 1:Nl
        distance_min = 1111111111111111111111111111111111111111;
        ncc_max = 0;
        nccd = 0;
        kd = 0;
        for d = 0:79
            init = 1;
            for fi = -2:2;
                for fj = -2:2
                    turni = i+fi;
                    turnj = j+fj;
                    if turni < 1
                        turni = 1;
                    end
                    if turnj < 1
                        turnj = 1;
                    end
                    if turni > Ml
                        turni = Ml;
                    end
                    if turnj > Nl
                        turnj = Nl;
                    end
                    FL(init) = left_image(turni,turnj);
                    turni = i+fi;
                    turnj = j-d+fj;
                     if turni < 1
                        turni = 1;
                    end
                    if turnj < 1
                        turnj = 1;
                    end
                    if turni > Ml
                        turni = Ml;
                    end
                    if turnj > Nl
                        turnj = Nl;
                    end
                    FR(init) = right_image(turni,turnj);
                    init = init+1;
                end
            end
            turn_distance = m_distance(FL,FR);
            turn_ncc = ncc(FL,FR);
            if (turn_distance < distance_min)
                 distance_min = turn_distance;
                 kd = d;  
            end
            if (turn_ncc > ncc_max)
                ncc_max = turn_ncc;
                nccd = d;
            end
        end
        SSD_img(i,j) = kd;
        NCC_img(i,j) = nccd;
    end
end
img_set{1} = SSD_img;
img_set{2} = NCC_img;
end
function [distance] = m_distance(FL, FR)
length = size(FL');
distance = 0;
for disti = 1:length
    distance = distance+(double(FL(disti)) - double(FR(disti)))^2;
end
end
function [distance] = ncc(FL, FR)
a = double(FL);
b = double(FR);
distance = (a*b')/((sqrt(a*a'))*(sqrt(b*b')));
end