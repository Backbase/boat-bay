import { ISource } from 'app/shared/model/source.model';

export interface ISourcePath {
  id?: number;
  name?: string;
  source?: ISource;
}

export class SourcePath implements ISourcePath {
  constructor(public id?: number, public name?: string, public source?: ISource) {}
}
