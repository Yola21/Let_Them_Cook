import { getDownloadURL, ref, uploadBytes } from "firebase/storage";
import { v4 } from "uuid";
import { imageStorage } from "../Firebase/config";

export const uploadImageToFirebase = async (image) => {
  const imageUUID = v4();
  const imageRef = ref(imageStorage, `files/${imageUUID}`);
  await uploadBytes(imageRef, image);
  return getImageURLFromFirebase(imageUUID);
};

export const getImageURLFromFirebase = async (imageUUID) => {
  const imageURL = await getDownloadURL(
    ref(imageStorage, `files/${imageUUID}`)
  );
  return imageURL;
};
