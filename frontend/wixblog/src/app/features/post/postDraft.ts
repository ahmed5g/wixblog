export interface PostDraft {
  title: string;
  blocks: any[]; // Editor.js JSON output
  status: 'saved' | 'saving' | 'error';
  lastSaved?: Date;
}
