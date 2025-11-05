import {Tag} from './tag';

export interface Post {
  id          : string;          // unique slug or guid
  title       : string;
  excerpt     : string;
  author      : string;
  authorLink  : string;
  date        : string;          // ISO date string
  category    : string;
  categoryLink: string;
  tags        : Tag[];
  images      : string[];        // relative paths
  link        : string;
}
