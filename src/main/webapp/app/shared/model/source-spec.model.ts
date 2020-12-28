import { Moment } from 'moment';
import { ISource } from 'app/shared/model/source.model';

export interface ISourceSpec {
  id?: number;
  url?: string;
  type?: string;
  name?: string;
  scannedOn?: Moment;
  downloaded?: boolean;
  checksum?: string;
  source?: ISource;
}

export class SourceSpec implements ISourceSpec {
  constructor(
    public id?: number,
    public url?: string,
    public type?: string,
    public name?: string,
    public scannedOn?: Moment,
    public downloaded?: boolean,
    public checksum?: string,
    public source?: ISource
  ) {
    this.downloaded = this.downloaded || false;
  }
}
