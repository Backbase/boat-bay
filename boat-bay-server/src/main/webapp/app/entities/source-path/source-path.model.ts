import { ISource } from 'app/entities/source/source.model';

export interface ISourcePath {
  id?: number;
  name?: string | null;
  source?: ISource | null;
}

export class SourcePath implements ISourcePath {
  constructor(public id?: number, public name?: string | null, public source?: ISource | null) {}
}

export function getSourcePathIdentifier(sourcePath: ISourcePath): number | undefined {
  return sourcePath.id;
}
