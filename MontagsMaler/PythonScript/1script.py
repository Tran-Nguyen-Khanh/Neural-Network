# Description: This script converts the ndjson files to png images.
#              To you use this script, you need to download the ndjson files from the google quickdraw dataset and put the script in the same folder as the ndjson files.
#              It also rotates and zooms in and out of the images.
#              The images are saved in the images folder.
#              The amount of images to process can be changed by changing the drawing_amount variable.
import json
import os
import matplotlib.pyplot as plt
import random
from PIL import Image
from PIL import Image, ImageOps

# directory containing the ndjson files
directory = "D:\\gs"

# amount of drawings to process
drawing_amount = 1000

# example of the data in the ndjson file
#   { 
#     "key_id":"5891796615823360",
#     "word":"nose",
#     "countrycode":"AE",
#     "timestamp":"2017-03-01 20:41:36.70725 UTC",
#     "recognized":true,
#     "drawing":[[[129,128,129,129,130,130,131,132,132,133,133,133,133,...]]]
#   }

# Crop the center of the image
def crop_center(img, crop_width, crop_height):
    width, height = img.size
    left = (width - crop_width)/2
    top = (height - crop_height)/2
    right = (width + crop_width)/2
    bottom = (height + crop_height)/2

    return img.crop((left, top, right, bottom))

def expand_and_gray_image(img, new_size, fill_color=255):  # Gray in grayscale
    new_img = Image.new('L', new_size, fill_color)
    new_img.paste(img, ((new_size[0]-img.size[0])//2,
                        (new_size[1]-img.size[1])//2))
    return new_img

# Convert the ndjson file to png images
# from one drawing function get 5 images:
# 1. original image
# 2. rotated image
# 3. rotated image
# 4. zoomed in image
# 5. zoomed out image
def convert_ndjson_to_png(ndjson_file, amount=20, rotation_angle=0):

    dir_name = os.path.splitext(ndjson_file)[0]
    output_directory = f'D:\\images\{dir_name}'
    
    # Create the output directory if it doesn't exist
    if not os.path.exists(output_directory):
        os.makedirs(output_directory)
    with open(ndjson_file, 'r') as file:
        counter = 0
        for line in file:
            # Parse each line as a JSON object
            data = json.loads(line)

            if counter < amount and data['recognized']:

                # Extract image data from JSON object
                #drawing_data = data['drawing']
                plt.figure(figsize=(1.28, 1.28))
                for stroke in data['drawing']:
                    plt.plot(stroke[0], [-y for y in stroke[1]], color='black')

                plt.axis('off')        
                # Save the plot as an image file
                key_id = data['key_id']
                plt.savefig(os.path.join(output_directory, f'{key_id}.png'))
                plt.close()



                # Generate a random angle between -35 and 0 degrees
                rotation_angle = random.uniform(-35, 0)
                # Open the saved image file and rotate it
                image = Image.open(os.path.join(output_directory, f'{key_id}.png'))
                rotated_image = image.rotate(rotation_angle, fillcolor='white')

                # Save the rotated image
                rotated_image.save(os.path.join(output_directory, f'{key_id}_{rotation_angle}.png'))

                # Generate a random angle between 0 and 35 degrees
                rotation_angle = random.uniform(0, 35)

                # Open the saved image file and rotate it
                image = Image.open(os.path.join(output_directory, f'{key_id}.png'))
                rotated_image = image.rotate(rotation_angle, fillcolor='white')

                # Save the rotated image
                rotated_image.save(os.path.join(output_directory, f'{key_id}_{rotation_angle}.png'))


                # Open the image file
                image = Image.open(os.path.join(output_directory, f'{key_id}.png'))


                # Zoom in by a factor of 2
                zoomed_in = ImageOps.scale(image, 1.5)
                cropped_img = crop_center(zoomed_in, 128, 128)
                cropped_img.save(os.path.join(output_directory, f'{key_id}_{"zoomed_in"}.png'))


                # Zoom out by a factor of 2
                zoomed_out = ImageOps.scale(image, 0.7)
                zoomed_out = expand_and_gray_image(zoomed_out, (128, 128))
                zoomed_out.save(os.path.join(output_directory, f'{key_id}_{"zoomed_out"}.png'))


                counter += 1
                print(f'Processed {counter} images. from {ndjson_file}')

# list to store all the data
datalist = []

for filename in os.listdir(directory):
    if filename.endswith(".ndjson"):
        #if filename == "line.ndjson" or filename == "star.ndjson" or filename == "snake.ndjson" or filename == "The Eiffel Tower.ndjson" or filename == "clock.ndjson":
        if filename == "door.ndjson" or filename == "hat.ndjson" or filename == "tree.ndjson" or filename == "radio.ndjson" or filename == "eyeglasses.ndjson":
            convert_ndjson_to_png(filename, drawing_amount)
            print(f'Finished processing {filename}.')
