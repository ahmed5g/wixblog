export type BlockType = 'text' | 'image' | 'video' | 'divider';

export interface PostBlock {
  id: string; // unique id
  type: BlockType;
  content?: string; // text, url, or base64
}
